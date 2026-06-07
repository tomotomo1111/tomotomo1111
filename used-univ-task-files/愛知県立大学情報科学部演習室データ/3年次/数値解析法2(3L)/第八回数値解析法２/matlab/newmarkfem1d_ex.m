clearvars;
close all;
% Newmark法例題
xitval = [0 1];
titval = [0 10];
cofp = @(x) 1;
cofq = @(x) 0;
srcf = @(x,t) -12*pi^2*sin(4*pi*t).*sin(2*pi*x);
iniu = @(x) 0;
iniv = @(x) 4*pi*sin(2*pi*x);
% 分割回数
mmax = 1;
hx = zeros(mmax,1);
l2err = zeros(mmax,1);
n = 20; nt = 400;
for m = 1:mmax
    [x,t,u,v,a] = newmark_fem1d(n,nt,xitval,titval,iniu,iniv,cofp,cofq,srcf);
    % 真の解行列
    exactu_mat = exactu(x,t);
    % L2誤差の計算
    dt = t(2)-t(1);
    hx(m) = x(2)-x(1);
    errmat = exactu_mat-u;
    l2err(m,1) = 0.0;
    for k = 2:nt+1
        l2errsp = 0.0;
        for i = 1:n
            l2errsp = l2errsp + errmat(i,k)^2 + errmat(i,k)*errmat(i+1,k)+errmat(i+1,k);
        end
        l2errsp = hx/3*l2errsp;
        if k == nt+1
            l2err(m,1) = l2err(m,1) + l2errsp(1,1);
        else
            l2err(m,1) = l2err(m,1) + 2.*l2errsp(1,1);
        end
    end
    l2err(m,1) = hx(m)/2.*l2err(m,1);
    l2err(m) = sqrt(l2err(m));
    fprintf('dt = %e h = %e:  L2 error = %17.15e\n',dt,hx(m),l2err(m));
    if m < mmax
        n = 2*n; nt = 20*n;
    end
end

% 真の解と近似解のプロット
fig=figure;
filename = 'fem1d_wave_example.gif';
for k = 1:nt+1
    plot(x,u(:,k),'o',x,exactu_mat(:,k),'-');
    xlim(xitval);
    ylim([-1.1 1.1]);
    title(['t = ',num2str((k-1)*dt,'%.4f')]);
    drawnow;
    frame = getframe(fig);
    im = frame2im(frame);
    [imind,cm] = rgb2ind(im,512);
     if k == 1
        imwrite(imind,cm,filename,'gif','DelayTime',0.1);
    else
        imwrite(imind,cm,filename,'gif','WriteMode','append','DelayTime',0.1);
    end
end

% 両対数グラフと収束オーダーの確認
%{
p1e = polyfit(log10(1.0./hx),log10(l2err),1);
fprintf('\nConv. order = %e\n',abs(p1e(1)));
xp1e = linspace(0.5*min(log10(1.0./hx)),1.25*max(log10(1.0./hx)),1000);
pp1e = polyval(p1e,xp1e);
figure;
plot(log10(1.0./hx),log10(l2err),"o");
hold on;
plot(xp1e,pp1e);
hold off
xlim([0.5*min(log10(1.0./hx)) 1.25*max(log10(1.0./hx))]);
ylim([1.2*min(log10(l2err)) 0.75*max(log10(l2err))]);
saveas(gcf,'fem1d_wave.png');
%}