clear;
[N]=meshgrid(1:16);
tn=48.7170;
ts=[48.7170 22.5342 17.2881 15.9825 11.5062 10.0785 9.0965 8.3843 7.6002 7.1209 7.0618 6.9421 6.2595 6.1114 6.0952 6.2074];
sn=ts./tn
kf=(N./sn-1)./(N-1)
plot(N, kf, "c-");colorbar;
xlabel('N'),ylabel('Kf'),
print('wk2-1.png','-dpng');
hold off;