clearvars;
close all;
% pdepe使用例
n = 20;
nt = 50;
lastT = 0.4;
x = linspace(0,1,n+2);
t = linspace(0,lastT,nt+1);
m = 0;
sol = pdepe(m,@heatfuncs,@heatic,@heatbc,x,t);
% 絶対誤差計算
kappa = 1;
exactmat = exactu_heat1d(x,t,kappa);
abserr = max(max(exactmat-sol));
dt = lastT/nt; dx = 1.0/(n+1);
r = kappa * dt/dx^2;
fprintf('パラメータ r = %e\n',r);
fprintf('dt = %e, dx = %e:  絶対値最大誤差 = %17.15e\n',dt,dx,abserr);
% 近似解のプロット
fig=figure;
filename = 'pdepe_example.gif';
for j = 1:nt+1
    plot(x,sol(j,:),'o',x,exactmat(j,:),'-');
    xlim([0 1]);
    ylim([0 1.1]);
    title(['t = ',num2str((j-1)*dt,'%.4f')]);
    drawnow;
    frame = getframe(fig);
    im = frame2im(frame);
    [imind,cm] = rgb2ind(im,512);
     if j == 1
        imwrite(imind,cm,filename,'gif','DelayTime',0.1);
    else
        imwrite(imind,cm,filename,'gif','WriteMode','append','DelayTime',0.1);
    end
end
