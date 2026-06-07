import rclpy, time, math
from rclpy.node import Node
from std_msgs.msg import String
from geometry_msgs.msg import Twist


class Management(Node):
    def __init__(self):
        super().__init__('management_node')
        self.sub = self.create_subscription(String, 'shape', self.callback, 10)
        self.pub_house = self.create_publisher(String, 'house', 1)
        self.pub_ball = self.create_publisher(String, 'ball', 1)
        self.pub_triangle = self.create_publisher(String, 'triangle', 1)
        self.pub_arrow = self.create_publisher(String, 'arrow', 1)
        self.pub_stickman = self.create_publisher(String, 'stickman', 1)

    def callback(self, msg_sub):
        msg = String()
        msg.data = 's'
        if msg_sub.data == 'house':
            self.pub_house.publish(msg)
        if msg_sub.data == 'ball':
            self.pub_ball.publish(msg)
        if msg_sub.data == 'triangle':
            self.pub_triangle.publish(msg)
        if msg_sub.data == 'arrow':
            self.pub_arrow.publish(msg)
        if msg_sub.data == 'stickman':
            self.pub_stickman.publish(msg)

def main():
    rclpy.init()
    node = Management()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        print('Ctrl＋cが押されました。')
    finally:
        node.destroy_node()
        rclpy.shutdown()
