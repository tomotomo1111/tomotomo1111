% 第2回講義例題（減衰振動）の真の解
function u = ex45u(t,gamma,omega0,u0)
    omega1 = sqrt(omega0^2-gamma^2);
    u(:,1) = exp(-gamma*t).*(u0(1)*cos(omega1*t)+(u0(2)+gamma*u0(1))/omega1*sin(omega1*t));
    u(:,2) = exp(-gamma*t).*(u0(2)*cos(omega1*t)-(gamma*u0(2)+omega0^2*u0(1))/omega1*sin(omega1*t));
end