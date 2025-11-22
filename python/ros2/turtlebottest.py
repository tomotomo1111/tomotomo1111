import rclpy
from rclpy.node import Node
from sensor_msgs.msg import LaserScan
from geometry_msgs.msg import Twist


class SampleProcessor(Node):
    def __init__(self):
        super().__init__('sample_processor_node')
        self.sub = self.create_subscription(
            LaserScan, 'scan', self.callback, 10) #トピック名 scan, メッセージ型 LaserScan
        self.pub = self.create_publisher(Twist, 'cmd_vel', 1) # トピック名 cmd_vel, メッセージ型 Twist

    def callback(self, msg_sub):
        range_min = float('inf') #ここから 
        for range in msg_sub.ranges:
            if range < range_min:
                range_min = range #ここまでは、ロボット正面から360度すべての中で、障害物との距離最小の値を求める。たぶん

        range_min_list = [] # 最小距離の角度(インデックス)を保存する。最小が複数あれば、それらすべて保存する。この際、インデックスは
        for i, range in enumerate(msg_sub.ranges): # ロボット正面からみた半時計周りの角度と一致する。
            if range == range_min:
                range_min_list.append(i)

        mean = sum(range_min_list)/len(range_min_list) # 障害物との距離最小を持つ角度の平均を求める

        msg_pub = Twist()

        msg_pub.linear.x = float(0.2) # とりあえず前進速度は0.2をいれておく
        if msg_sub.ranges[0] < 0.5: # msg_sub.ranges[0] は 0 が恐らく角度(インデックス)でロボット正面の障害物距離
            msg_pub.linear.x = float(0) # 前進速度を0にする。つまり停止
            msg_pub.angular.z = float(1.57) # たぶん半時計回転。ワールドを半時計にぐるぐるしてたから
        elif range_min < 0.5: # 前進方向の障害物がまぁまぁ離れている際に、全方向での障害物との距離最小が0.5を切っている場合に、
            if mean < 180: # 正面からみて左側のほうが右側よりも障害物が近いとみなせるなら、
                msg_pub.angular.z = float(-0.157) # 右に回転する(前進はする0.2)
            else:
                msg_pub.angular.z = float(0.157) # そうじゃないなら、左に回転する(前進はする0.2)
        else:
            msg_pub.angular.z = float(0.0) # 全方向にまぁまぁ近い距離に障害物がなければ角度は変更しない。多分これがないと回転し続けるのではぁ
        
        self.get_logger().info(f'Linear: {msg_pub.linear.x}, Angular: {msg_pub.angular.z}') # f' {}' はc言語の "%s "みたいにかける

        self.pub.publish(msg_pub)

def main():
    rclpy.init()
    node = SampleProcessor()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        print('Ctrl＋cが押されました。')
    finally:
        node.destroy_node()
        rclpy.shutdown()