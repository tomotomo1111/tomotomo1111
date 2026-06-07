% 右辺ベクトル値関数 f(t,u)
function dudt = odefcn(t,u,gamma,omega0)
    dudt = zeros(2,1);
    dudt(1) = u(2);
    dudt(2) = -2 * gamma * u(2) - omega0^2 * u(1);
end