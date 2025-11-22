output = [simout.Time simout.Data];
save('sim-p1p2n2n2IM3.dat', 'output','-ascii');
SimData = load(['sim-p1p2n2n2IM3.dat'], '-ascii');
plot(SimData(:,1), SimData(:,2:3), 'Linewidth', 1.5)
xlabel('time (s)')
ylim([0 2])
ylabel('reference & output (V)')
legend('reference,', 'output (p1 = -2 - 3i, p2 = -2 + 3i)');
grid on

print -dpng 'simp1p2n2n2IM3.png'