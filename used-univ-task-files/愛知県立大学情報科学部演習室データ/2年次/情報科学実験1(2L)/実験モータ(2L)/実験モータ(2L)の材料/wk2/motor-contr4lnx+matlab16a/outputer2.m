output = [simout.Time simout.Data];
save('sim-kyou75.dat', 'output','-ascii');
SimData = load(['sim-kyou75.dat'], '-ascii');
plot(SimData(:,1), SimData(:,2:3), 'Linewidth', 1.5)
xlabel('time (s)')
ylim([0 2])
ylabel('reference & output (V)')
legend('reference,', 'output (p1 = -7 - 5i, p2 = -7 + 5i)');
grid on

print -dpdf 'sim-kyou75.pdf'