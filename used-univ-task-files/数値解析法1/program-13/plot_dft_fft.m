close all; clear;

% 変数へ結果を代入
filename_dft = 'exc-dft.dat';
filename_fft = 'exc-fft.dat';
delimiterIn = ' ';
dft_result = importdata(filename_dft,delimiterIn);
fft_result = importdata(filename_fft,delimiterIn);

% データ数
[N,~] = size(dft_result);

% 振幅スペクトルのプロット
figure;
plot(dft_result(1:N/2,1),dft_result(1:N/2,4));
xlabel('Frequency');
ylabel('Magnitude');
legend('DFT');
xlim([0 dft_result(N/2,1)]);
exportgraphics(gcf,'dft_result.png');
figure;
plot(fft_result(1:N/2,1),fft_result(1:N/2,4));
xlabel('Frequency');
ylabel('Magnitude');
legend('FFT');
xlim([0 fft_result(N/2,1)]);
exportgraphics(gcf,'fft_result.png');
