import rclpy
from rclpy.node import Node
from std_msgs.msg import String

class PracticePublisher(Node):
    def __init__(self):
        super().__init__('practice_publisher_node')
        self.pub = self.create_publisher(String, 'practice_topic', 10)
        self.timer = self.create_timer(1, self.timer_callback)
        self.i = 10
    def timer_callback(self):
        msg = String()
        if self.i > 0:
            msg.data = f'i = {self.i}'
        else:
            msg.data = f'Over and out!'
            self.destroy_timer(self.timer)
        self.pub.publish(msg)
        self.get_logger().info(f'Publish: {msg.data}')
        self.i -= 1
def main():
    print('========== プログラム開始 ==========')
    rclpy.init()
    node = PracticePublisher()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        print('Ctrl＋cが押されました。')
    finally:
        node.destroy_node()
        rclpy.shutdown()
        print('========== プログラム終了 ==========')