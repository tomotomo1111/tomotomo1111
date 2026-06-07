
SimDataA = load('kkadai14.dat', '-ascii');

figure
plot(SimDataA(:,1), SimDataA(:,2), '-bo', 'Linewidth', 1.0)
xlabel('Stimulus intensity [\muA/cm^2]')
ylim([0 60])
ylabel('firing rate [Hz]')
legend('reference',...
        'Location','northwest',...
        'Orientation','horizontal','NumColumns',1)
set(findobj('type','axes'),'fontsize',8)
grid on

print -dpdf 'kkadai14(1).pdf'
