
SimDataA = load('sim-p1p2n2n2IM1.dat', '-ascii');
SimDataB = load('sim-p1p2n2n2IM2.dat', '-ascii');
SimDataC = load('sim-p1p2n2n2IM3.dat', '-ascii');
SimDataD = load('sim-p1p2n2n2IM05.dat', '-ascii');

figure
plot(SimDataA(:,1), SimDataA(:,2:3), 'Linewidth', 1.5), hold on
plot(SimDataB(:,1),SimDataB(:,3),'Linewidth', 1.5)
plot(SimDataC(:,1),SimDataC(:,3),'Linewidth', 1.5)
plot(SimDataD(:,1),SimDataD(:,3),'Linewidth', 1.5)
xlabel('time (s)')
ylim([0 2])
ylabel('reference & output (V)')
legend('reference,', 'output (p1=-2-i, p2=-2+i)',...
        'output (p1=-2-2i, p2 =-2+2i)','output (p1=-2-3i, p2=-2+3i)',...
        'output (p1=-2-0.5i, p2=-2+0.5i)',...
        'Location','southeast',...
        'Orientation','horizontal','NumColumns',2)
set(findobj('type','axes'),'fontsize',8)
grid on

print -dpdf 'p1p2-image.pdf'
