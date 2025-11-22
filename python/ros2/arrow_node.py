import rclpy, time, math
from rclpy.node import Node
from std_msgs.msg import String
from geometry_msgs.msg import PoseStamped


class Arrow(Node):
    def __init__(self):
        super().__init__('arrow_node')
        self.sub = self.create_subscription(String, 'arrow', self.callback, 1)
        self.pub = self.create_publisher(PoseStamped, 'goal_pose', 10)
        self.angle_deg = 90.0
        self.x = 0.0
        self.y = 0.0

    def callback(self, msg_sub):
        msg_pub = PoseStamped()

        self.goStraight(10.5, msg_pub)

        self.rotate(-120, msg_pub)
        self.goStraight(10.5, msg_pub)

        self.rotate(-120, msg_pub)
        self.goStraight(10.5, msg_pub)

        self.rotate(-120, msg_pub)
        self.goStraight(3.5, msg_pub)

        self.rotate(90, msg_pub)
        self.goStraight(10.5, msg_pub)

        self.rotate(-90, msg_pub)
        self.goStraight(3.5, msg_pub)

        self.rotate(-90, msg_pub)
        self.goStraight(10.5, msg_pub)

        self.rotate(-90, msg_pub)
        self.goStraight(7.0, msg_pub)

        self.rotate(-180, msg_pub)
    
    def rotate(self, angle_deg, msg_pub):
        destination_angle_deg = self.angle_deg + angle_deg
        destination_angle_deg %= 360
        roll = pitch = 0.0
        yaw = math.radians(destination_angle_deg)
        quaternion_x = math.sin(roll/2) * math.cos(pitch/2) * math.cos(yaw/2) - math.cos(roll/2) * math.sin(pitch/2) * math.sin(yaw/2)
        quaternion_y = math.cos(roll/2) * math.sin(pitch/2) * math.cos(yaw/2) + math.sin(roll/2) * math.cos(pitch/2) * math.sin(yaw/2)
        quaternion_z = math.cos(roll/2) * math.cos(pitch/2) * math.sin(yaw/2) - math.sin(roll/2) * math.sin(pitch/2) * math.cos(yaw/2)
        quaternion_w = math.cos(roll/2) * math.cos(pitch/2) * math.cos(yaw/2) + math.sin(roll/2) * math.sin(pitch/2) * math.sin(yaw/2)

        msg_pub.header.frame_id = 'map'
        msg_pub.header.stamp = self.get_clock().now().to_msg()
        msg_pub.pose.position.x = self.x
        msg_pub.pose.position.y = self.y
        msg_pub.pose.position.z = 0.0
        msg_pub.pose.orientation.x = quaternion_x
        msg_pub.pose.orientation.y = quaternion_y
        msg_pub.pose.orientation.z = quaternion_z
        msg_pub.pose.orientation.w = quaternion_w

        i = 1
        while i < 11:
            self.get_logger().info(f'Rotating: ({self.angle_deg}, {self.angle_deg + angle_deg})')
            self.pub.publish(msg_pub)
            time.sleep(1.0)
            i += 1
        self.angle_deg = destination_angle_deg
        
        
    
    def goStraight(self, timef, msg_pub):
        yaw = math.radians(self.angle_deg)
        diff_y = math.sin(yaw) * float(0.2) * timef
        diff_x = math.cos(yaw) * float(0.2) * timef
        destination_x = self.x + diff_x
        destination_y = self.y + diff_y

        msg_pub.header.frame_id = 'map'
        msg_pub.header.stamp = self.get_clock().now().to_msg()
        msg_pub.pose.position.x = destination_x
        msg_pub.pose.position.y = destination_y
        msg_pub.pose.position.z = 0.0
        msg_pub.pose.orientation.x = 0.0
        msg_pub.pose.orientation.y = 0.0
        msg_pub.pose.orientation.z = 0.0
        msg_pub.pose.orientation.w = 1.0

        i = 1
        while i < 11:
            self.get_logger().info(f'Moving Destination: ({msg_pub.pose.position.x}, {msg_pub.pose.position.y})')
            self.pub.publish(msg_pub)
            time.sleep(1.0)
            i += 1
        self.x = destination_x
        self.y = destination_y
        


def main():
    rclpy.init()
    node = Arrow()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        print('Ctrl＋cが押されました。')
    finally:
        node.destroy_node()
        rclpy.shutdown()