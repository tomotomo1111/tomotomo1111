
SimDataA = load('simu-kadai5-2c.dat', '-ascii');
SimDataB = load('simu-kadai5-2cc.dat', '-ascii');
%SimDataC = load('simu-kadai5-2b.dat', '-ascii');
%SimDataD = load('simu-kadai5-2bb.dat', '-ascii');
%SimDataE = load('simu-kadai2-2v0.dat', '-ascii');


figure
plot(SimDataA(:,1), SimDataA(:,2),'Linewidth', 1.0), hold on
plot(SimDataB(:,1), SimDataB(:,2),'Linewidth', 1.0)
plot(SimDataA(:,1), SimDataA(:,3),'Linewidth', 1.0)
plot(SimDataB(:,1), SimDataB(:,3),'Linewidth', 1.0)
%plot(SimDataE(:,2), SimDataE(:,3),'Linewidth', 1.5)
xlabel('t')
ylim([-2.4 3.6])
ylabel('x, y(m)')
legend('output (vbx(0) = 0.0 cx)',...
        'output (vbx(0) = 0.00001 cx)',...
        'output (vbx(0) = 0.0 cy)',...
        'output (vbx(0) = 0.00001 cy)',...
        'Location','southeast',...
        'Orientation','horizontal','NumColumns',2)
set(findobj('type','axes'),'fontsize',8)
grid on

print -dpdf 'simu-kadai5-2c.pdf'
