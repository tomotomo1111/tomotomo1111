output = [out.simout.Time out.simout.Data];
save('kkadai21-i.dat', 'output','-ascii');
SimData = load(['kkadai21-i.dat'], '-ascii');
plot(SimData(:,1), SimData(:,7:8), 'Linewidth', 1.5)
xlabel('time (s)')
ylim([-50 100])
ylabel('reference & output (V)')
%legend('reference,', 'output (p1 = -2 - 3i, p2 = -2 + 3i)');
grid on
%確認用png
print -dpng 'kkadai21-c.png'