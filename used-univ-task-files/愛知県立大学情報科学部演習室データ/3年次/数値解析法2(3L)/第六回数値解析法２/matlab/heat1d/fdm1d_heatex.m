clearvars;
close all;
% 差分法（1次元熱伝導問題）例題
% 例題設定
itval = [0 1];
kappa = 1;
lastT = 0.4;
% 初期値関数
u0func = @(x) sin(pi*x);
% 空間・時間分割数
n = 20; nt = 400;
% 差分法による近似計算
[tfdm, xfdm, ufdm] = fdm1d_heat(n,itval,nt,lastT,kappa,u0func);
% 絶対誤差計算
exactmat = exactu_heat1d(xfdm,tfdm,kappa);
abserr = max(max(exactmat-ufdm));
dt = lastT/nt; dx = (itval(2)-itval(1))/(n+1);
fprintf('dt = %e, dx = %e: 絶対値最大誤差 = %17.15e\n',dt,dx,abserr);
% 近似解のプロット
fig=figure;
filename = 'fdm1d_heat_example.gif';
for j = 1:nt+1
    plot(xfdm,ufdm(j,:),'o-',xfdm,exactmat(j,:),'-');
    xlim(itval);
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
