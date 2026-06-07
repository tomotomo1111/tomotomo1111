clear all; 
close all;

% model parameter
np = 1000; %total population
k = 0.01;  %constant parameter

% initial value
y = 1; 

% time span
tfinal = 1.5; n = 500;
h = tfinal / n;

% Euler's method
for i=1:n
  dydt = k*(np-y)*y;
  y = y + h*dydt;
  yy(i) = y; tt(i) = i*h;
end

figure(1);
plot(tt,yy,'-r');
title('Rumor spread');
xlabel('time'); ylabel('Populations');
ylim([0 1100]); 
yticks([0:100:1000]);
xticks([0:0.2:1.4]);


