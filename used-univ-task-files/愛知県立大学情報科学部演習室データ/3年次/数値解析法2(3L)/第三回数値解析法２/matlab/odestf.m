% 右辺ベクトル値関数 f(t,u)
function dudt = odestf(t,u)
    A=[[-2.0002 1.9998];[1.9998 -2.0002]];
    dudt = A * u;
end