% File: 
%   lotkavolterraode.m
% Usage:
%   In the matlab command window, execute this script as follows:
%   >> lotkavolterraode;
%
%
function [t, y] = lotkavolterraode()
%
yinit(1) = 20; %Predetor
yinit(2) = 20; %Prey

% Settings for integration
tstep = 0.01; tlast = 30;
tspan = 0.0 : tstep : tlast;
option = odeset('MaxStep',0.1*tstep,'Stats','on','BDF','off');
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    [t y] = ode45( @lotkavolterra, tspan, yinit, option );
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

figure(1);
subplot(2,1,1);
plot(t(:,1),y(:,1),'-r'); 
hold on;
plot(t(:,1),y(:,2),'-g');
title('Predetor/Prey populations');
xlabel('t'); ylabel('Populations');
legend('Predetor','Prey');
hold off;
subplot(2,1,2);
plot(y(:,2),y(:,1)); 
xlabel('Prey'); ylabel('Predetor');


end

function ydot = lotkavolterra( t, y )

% parameters
a1 = 1; a2 = 1;
b1 = 0.02; b2 = 0.01;

x1 = y(1);
x2 = y(2);

dxdt = -( a1 - b1*x2 ) * x1;
dydt =  ( a2 - b2*x1 ) * x2;

ydot = [dxdt; ...
        dydt];

end