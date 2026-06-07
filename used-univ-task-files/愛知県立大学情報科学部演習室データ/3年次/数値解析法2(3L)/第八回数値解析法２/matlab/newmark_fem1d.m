function [x,t,u,v,a] = newmark_fem1d(n,nt,xitval,titval,iniu,iniv,cofp,cofq,srcf)
    beta = 1.0/4; gam = 0.5; % 平均加速度法
    %beta = 1.0/6; gam = 0.5; % 線形加速度法
    %beta = 1.0/12; gam = 0.5; % Royal road 法
    x = linspace(xitval(1),xitval(2),n+1);
    t = linspace(titval(1),titval(2),nt+1);
    utmp = zeros(n-1,nt+1);
    vtmp = zeros(n-1,nt+1);
    atmp = zeros(n-1,nt+1);
    utmp(:,1) = iniu(x(2:n))';
    vtmp(:,1) = iniv(x(2:n))';
    dt = t(2)-t(1);
    [matM, matA] = fem1d_matrix(x,cofp,cofq);
    fmat = fem1d_rvec(x,t,srcf);
    atmp(:,1) = matM\(fmat(:,1) - matA*utmp(:,1));

    %
    % ニューマーク法プログラムを完成させてください
    %

    u = [zeros(1,nt+1);utmp;zeros(1,nt+1)];
    v = [zeros(1,nt+1);vtmp;zeros(1,nt+1)];
    a = [zeros(1,nt+1);atmp;zeros(1,nt+1)];
end