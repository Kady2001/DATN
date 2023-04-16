import argparse
from inference import Transformer


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('--checkpoint', type=str, default='D:/Nghien Cuu KH 2022/pytorch-animeGAN/content/checkpoints/generator_hayao.pth')
    parser.add_argument('--src', type=str, default='D:/Nghien Cuu KH 2022/pytorch-animeGAN/example/real_vid/1-Minute Nature Background Sound.mp4', help='Path to input video')
    parser.add_argument('--dest', type=str, default='D:/Nghien Cuu KH 2022/pytorch-animeGAN/example/myreslut', help='Path to save new video')
    parser.add_argument('--batch-size', type=int, default=2)
    parser.add_argument('--start', type=int, default=0, help='Start time of video (second)')
    parser.add_argument('--end', type=int, default=0, help='End time of video (second), 0 if not set')

    return parser.parse_args()


def main(args):
    Transformer(args.checkpoint).transform_video(args.src, args.dest,
                                                 args.batch_size,
                                                 start=args.start,
                                                 end=args.end)

if __name__ == '__main__':
    args = parse_args()
    main(args)
