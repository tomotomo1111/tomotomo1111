clear all; 
close all;

% model parameter
% a1 = 2; a2 = 10;
% b1 = 0.001; b2 = 0.04;
a1 = 1; a2 = 1;
b1 = 0.02; b2 = 0.01;

% initial value
% x = 400; y = 4000;
x = 20; y = 20;
xe = x; ye = y;

% time span
tfinal = 30; n = 1000;
h = tfinal / n;

% Euler's method
for i=1:n
  [dxdt, dydt] = lotkavolterra(a1,a2,b1,b2,x,y);
  x = x + h*dxdt;
  y = y + h*dydt;
  xx(i) = x; yy(i) = y; tt(i) = i*h;
end

figure(1);
subplot(2,1,1);
plot(tt,xx,'-r'); 
hold on;
plot(tt,yy,'-g');
title('Predetor/Prey populations');
xlabel('t'); ylabel('Populations');
legend('Predetor','Prey');
hold off;
subplot(2,1,2);
plot(yy,xx); 
xlabel('Prey'); ylabel('Predetor');

pause(1);

% improved Euler's method
x = xe; y = ye;
for i=1:n
  [xk1, yk1] = lotkavolterra(a1,a2,b1,b2,x,y);
  [xk2, yk2] = lotkavolterra(a1,a2,b1,b2,x+h*xk1,y+h*yk1);
  x = x + h*(xk1 + xk2) / 2;
  y = y + h*(yk1 + yk2) / 2;
  xx(i) = x; yy(i) = y; tt(i) = i*h;
end

figure(2);
subplot(2,1,1)
plot(tt,xx,'-r'); 
hold on;
plot(tt,yy,'-g');
hold off;
subplot(2,1,2);
plot(yy,xx);

function [dxdt, dydt] = lotkavolterra(a1,a2,b1,b2,x,y)
    dxdt = -(a1 - b1*y)*x;
    dydt =  (a2 - b2*x)*y;
end
    


