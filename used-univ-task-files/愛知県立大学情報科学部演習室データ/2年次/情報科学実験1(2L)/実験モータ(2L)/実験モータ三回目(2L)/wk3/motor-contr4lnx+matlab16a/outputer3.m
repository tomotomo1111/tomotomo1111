
SimDataA = load('sim-p1p2n2n2.dat', '-ascii');
SimDataB = load('sim-p1p2n1n1.dat', '-ascii');
SimDataC = load('sim-p1p2p1p1.dat', '-ascii');
SimDataD = load('sim-p1p2p2p2.dat', '-ascii');

figure
plot(SimDataA(:,1), SimDataA(:,2:3), 'Linewidth', 1.5), hold on
plot(SimDataB(:,1),SimDataB(:,3),'Linewidth', 1.5)
plot(SimDataC(:,1),SimDataC(:,3),'Linewidth', 1.5)
plot(SimDataD(:,1),SimDataD(:,3),'Linewidth', 1.5)
xlabel('time (s)')
ylim([0 3])
ylabel('reference & output (V)')
legend('reference,', 'output (p1=p2=-2)',...
        'output (p1=p2=-1)','output (p1=p2=1)',...
        'output (p1=p2=2)',...
        'Location','northeast',...
        'Orientation','horizontal','NumColumns',2)
set(findobj('type','axes'),'fontsize',8)
grid on

print -dpdf 'p1p2-equal-0.pdf'
