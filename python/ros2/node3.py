import rclpy
from rclpy.node import Node
from std_msgs.msg import String

class PracticeSubscriber(Node):
    def __init__(self):
        super().__init__('practice_subscriber_node')
        self.sub = self.create_subscription(String, 'practice_topic', self.callback, 10)
    def callback(self, msg):
        self.get_logger().info(f'Subscribe: {msg.data}')
        while True:
            try:
                self.get_logger().info('1 : hit, 2 : stand')
                choice = int(input("1から2の数字を入力してください: "))
                if 1 <= choice <= 2:
                    self.get_logger().info(f'選択された数字: {choice}')
                    break
                else:
                    self.get_logger().info('1から2の数字を入力してください。')
            except ValueError:
                self.get_logger().info('有効な数字を入力してください。')

def main():
    print('========== プログラム開始 ==========')
    rclpy.init()
    node = PracticeSubscriber()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        print('Ctrl＋cが押されました。')
    finally:
        node.destroy_node()
        rclpy.shutdown()
        print('========== プログラム終了 ==========')