%% velo_pi_mbd.m

%% Initialize & load data
close all
clear all
load model_data

%% PI design by pole placement
p1 = input('p1 = ');
%p2 = p1; % 重根の場合
p2 = input('p2 = '); % p2も指定する場合

Kp = -((p1+p2)*T + 1)/K;
Ki = p1*p2*T/K;

%% Display PI parameters
disp('>>> PI parameters <<<')
fprintf('Kp  = %f\n',Kp);
fprintf('Ki  = %f\n',Ki);

%% Open simulink model
open_system('velo_pi_mbd_sl');
open_system('velo_pi_mbd_sl/Output');

%% Experiment
ts = 1/50;
sim('velo_pi_mbd_sl')

%% EOF of velo_pi_mbd.m