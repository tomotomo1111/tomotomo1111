
SimDataA = load('kkadai13-n96k28-8.dat', '-ascii');
SimDataB = load('kkadai13-n120k28-8.dat', '-ascii');
SimDataC = load('kkadai13-n144k28-8.dat', '-ascii');
SimDataD = load('kkadai13-n96k36.dat', '-ascii');
SimDataE = load('kkadai13-n120k36.dat', '-ascii');
SimDataF = load('kkadai13-n144k36.dat', '-ascii');
SimDataG = load('kkadai13-n96k43-2.dat', '-ascii');
SimDataH = load('kkadai13-n120k43-2.dat', '-ascii');
SimDataI = load('kkadai13-n144k43-2.dat', '-ascii');

figure
plot(SimDataA(:,1), SimDataA(:,2),'Linewidth', 1.0), hold on
plot(SimDataB(:,1), SimDataB(:,2),'Linewidth', 1.0)
plot(SimDataC(:,1), SimDataC(:,2),'Linewidth', 1.0)
plot(SimDataD(:,1), SimDataD(:,2),'Linewidth', 1.0)
plot(SimDataE(:,1), SimDataE(:,2),'Linewidth', 1.0)
plot(SimDataF(:,1), SimDataF(:,2),'Linewidth', 1.0)
plot(SimDataG(:,1), SimDataG(:,2),'Linewidth', 1.0)
plot(SimDataH(:,1), SimDataH(:,2),'Linewidth', 1.0)
plot(SimDataI(:,1), SimDataI(:,2),'Linewidth', 1.0)
xlabel('time[msec]')
ylim([-120 80])
ylabel('Membrace Potential[mV]')
legend('output (gNa = 96, gK = 28.8)',...
        'output (gNa = 120, gK = 28.8)',...
        'output (gNa = 144, gK = 28.8)',...
        'output (gNa = 96, gK = 36)',...
        'output (gNa = 120, gK = 36)',...
        'output (gNa = 144, gK = 36)',...
        'output (gNa = 96, gK = 43.2)',...
        'output (gNa = 120, gK = 43.2)',...
        'output (gNa = 144, gK = 43.2)',...
        'Location','northeast',...
        'Orientation','horizontal','NumColumns',3)
set(findobj('type','axes'),'fontsize',8)
grid on

print -dpdf 'kkadai13(9).pdf'
