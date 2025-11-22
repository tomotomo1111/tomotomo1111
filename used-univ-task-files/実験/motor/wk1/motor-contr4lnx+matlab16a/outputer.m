output = [simout.Time simout.Data];
save('sim-ki100.dat', 'output','-ascii');
SimData = load('sim-ki100.dat', '-ascii');
plot(SimData(:,1), SimData(:,2), SimData(:,1), SimData(:,3))
xlabel('time (s)')
xlabel('time (s)')
ylim([0 2])
ylabel('reference & actual signals (v)')
legend('reference,', 'actual')
grid on
print -deps 'sim-ki100.eps'