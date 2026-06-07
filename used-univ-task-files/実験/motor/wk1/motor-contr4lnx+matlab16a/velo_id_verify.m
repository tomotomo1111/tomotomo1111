%% velo_id_verify.m

%% Set identified parameters
K        = 1.57;
T        = 0.76;
u_offset = 1.03;

%% Open simulink model
open_system('velo_id_verify_sl');
open_system('velo_id_verify_sl/Output');

%% Start experiment
sim('velo_id_verify_sl')

%% Save Parameters
save model_data K T u_offset

%% EOF of velo_id_verify.m