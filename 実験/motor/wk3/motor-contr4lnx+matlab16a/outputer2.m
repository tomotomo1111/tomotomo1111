output = [simout.Time simout.Data];
save('sim-nonmodel-1174.dat', 'output','-ascii');
SimData = load(['sim-nonmodel-1174.dat'], '-ascii');
plot(SimData(:,1), SimData(:,2:3), 'Linewidth', 1.5)
xlabel('time (s)')
ylim([0 2])
ylabel('reference & output (V)')
legend('reference,', 'output (p1 = -10 - 5i, p2 = -10 + 5i)');
grid on

print -dpdf 'sim-nonmodel-1174.pdf'