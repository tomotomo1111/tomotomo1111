% 第4回講義例題（硬い問題）の真の解 ※第4回と同じ
function u = exstf(t)
    lam1 = -4; lam2 = -4.0e-4;
    c1 = 1; c2 = 1;
    u1 = [-1 1]; u2 = [1 1];
    u(:,1) = c1*u1(1)*exp(lam1*t)+c2*u2(1)*exp(lam2*t);
    u(:,2) = c1*u1(2)*exp(lam1*t)+c2*u2(2)*exp(lam2*t);
end