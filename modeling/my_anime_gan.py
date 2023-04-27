
import torch.nn as nn
import torch.nn.functional as F
from torch.nn.utils import spectral_norm
from modeling.conv_blocks import DownConv
from modeling.conv_blocks import UpConv
from modeling.conv_blocks import SeparableConv2D
from modeling.conv_blocks import InvertedResBlock
from modeling.conv_blocks import ConvBlock
from utils.common import initialize_weights



class EncoderBlock(nn.Module):
    def __init__(self, in_channels, out_channels, bias=False, use_head_attention=False):
        super(EncoderBlock, self).__init__()
        self.conv1 = nn.Conv2d(in_channels, out_channels, kernel_size=3, stride=1, padding=1, bias=bias)
        self.conv2 = nn.Conv2d(out_channels, out_channels, kernel_size=3, stride=1, padding=1, bias=bias)
        self.relu = nn.ReLU(inplace=True)
        self.head_attention = None
        if use_head_attention:
            self.head_attention = nn.MultiheadAttention(out_channels, 8)

    def forward(self, x):
        out = self.conv1(x)
        out = self.relu(out)
        out = self.conv2(out)
        out = self.relu(out)
        if self.head_attention is not None:
            out, _ = self.head_attention(out, out, out)
        return out

class Generator(nn.Module):
    def __init__(self, dataset='', use_head_attention=False):
        super(Generator, self).__init__()
        self.name = f'generator_{dataset}'
        bias = False

        self.encode_blocks = nn.Sequential(
            EncoderBlock(3, 64, bias=bias, use_head_attention=use_head_attention),
            EncoderBlock(64, 128, bias=bias, use_head_attention=use_head_attention),
            nn.MaxPool2d(kernel_size=2, stride=2),
            EncoderBlock(128, 128, bias=bias, use_head_attention=use_head_attention),
            EncoderBlock(128, 256, bias=bias, use_head_attention=use_head_attention),
            nn.MaxPool2d(kernel_size=2, stride=2),
            EncoderBlock(256, 256, bias=bias, use_head_attention=use_head_attention),
        )

        self.res_blocks = nn.Sequential(
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
            InvertedResBlock(256, 256, bias=bias),
        )

        self.decode_blocks = nn.Sequential(
            nn.Upsample(scale_factor=2, mode='bilinear', align_corners=True),
            EncoderBlock(256, 128, bias=bias),
            nn.Upsample(scale_factor=2, mode='bilinear', align_corners=True),
            EncoderBlock(128, 128, bias=bias),
            EncoderBlock(128, 64, bias=bias),
            nn.Conv2d(64, 3, kernel_size=3, stride=1, padding=1, bias=bias),
            nn.Tanh(),
        )

        initialize_weights(self)

    def forward(self, x):
        out = self.encode_blocks(x)
        out = self.res_blocks(out)
        img = self.decode_blocks(out)

        return img


class Discriminator(nn.Module):
    def __init__(self,  args):
        super(Discriminator, self).__init__()
        self.name = f'discriminator_{args.dataset}'
        self.bias = False
        channels = 32

        layers = [
            nn.Conv2d(3, channels, kernel_size=3, stride=1, padding=1, bias=self.bias),
            nn.LeakyReLU(0.2, True)
        ]

        for i in range(args.d_layers):
            layers += [
                nn.Conv2d(channels, channels * 2, kernel_size=3, stride=2, padding=1, bias=self.bias),
                nn.LeakyReLU(0.2, True),
                nn.Conv2d(channels * 2, channels * 4, kernel_size=3, stride=1, padding=1, bias=self.bias),
                nn.InstanceNorm2d(channels * 4),
                nn.LeakyReLU(0.2, True),
            ]
            channels *= 4

        layers += [
            nn.Conv2d(channels, channels, kernel_size=3, stride=1, padding=1, bias=self.bias),
            nn.InstanceNorm2d(channels),
            nn.LeakyReLU(0.2, True),
            nn.Conv2d(channels, 1, kernel_size=3, stride=1, padding=1, bias=self.bias),
        ]

        if args.use_sn:
            for i in range(len(layers)):
                if isinstance(layers[i], nn.Conv2d):
                    layers[i] = spectral_norm(layers[i])

        self.discriminate = nn.Sequential(*layers)

        initialize_weights(self)

    def forward(self, img):
        return self.discriminate(img)