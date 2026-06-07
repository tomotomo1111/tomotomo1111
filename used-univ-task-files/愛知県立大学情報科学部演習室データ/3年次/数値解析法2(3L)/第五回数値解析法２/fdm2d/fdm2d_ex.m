clearvars;clf;close all;
% 2次元差分法
% 例題設定
xitv = [0 1]; yitv = [0 1];
nx = 128; ny = 128;
srcf = @(x,y) -5 * pi^2 * sin(pi * x) * sin(2 * pi * y);
bdg = @(x,y) -sin(pi * x) * sin(2 * pi * y);

% 差分法による近似解計算
[gdx,gdy,ufdm2d] = fdm2d_Poisson(xitv,yitv,nx,ny,srcf,bdg);

% 絶対誤差の計算
umat = exactu2d(gdx,gdy);
abserr = max(max(abs(umat-ufdm2d)));
fprintf('h = %e: Max error = %17.15e\n',1.0/(nx+1),abserr);

% 計算結果プロット
surfc(gdx,gdy,ufdm2d);
zlim([-1.1 1.1]);
saveas(gcf,'sol_fdm2d.png');
