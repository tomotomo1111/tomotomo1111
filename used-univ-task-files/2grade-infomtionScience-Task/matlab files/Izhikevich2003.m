clear all; 
close all;

% model parameter
a = 0.02;
b = 0.2;
c = -55.0;
d = 4.0;

% initial value
v = -55; u = -10;

% time span
tfinal = 100; n = 1000000;
h = tfinal / n;

% Euler's method
for i=1:n
  [dvdt, dudt] = Izhikevich(a,b,c,d,v,u);
  
  if v >= 30.0
      v = c;
      u = u + d;
  else
      v = v + h*dvdt;
      u = u + h*dudt;
  end 
  xx(i) = v; yy(i) = u; tt(i) = i*h;
end

figure(1);

plot(tt,xx,'-r'); 


function [dvdt, dudt] = Izhikevich(a,b,c,d,v,u)
    Iapp = 20;
    dvdt = 0.04*v*v + 5*v + 140 -u + Iapp;
    dudt = a*( b*v - u );
end
    


