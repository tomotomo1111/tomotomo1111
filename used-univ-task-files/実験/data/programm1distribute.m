clear;

addpath('/cis/public/datasci/jikken/L8data');

inMatlab=mapreducer(0);

tdir='/cis/public/datasci/class/taxi_yellow/2017/';
path1={[tdir 'yellow_tripdata_2017-01.csv'],...
    [tdir 'yellow_tripdata_2017-02.csv']};

tic
ds=datastore(path1);
ds.SelectedVariableNames={'PULocationID', 'total_amount'};

ticBytes(gcp)
output=mapreduce(ds,@statmapper,@statreducer,inMatlab);
tocBytes(gcp)

r=readall(output); % Read output data
id=1:265; % Define number of zone
a=[id;r.Value{:}.fare]; % Create array of ID and fare
tmp=r.Value{:}.num<100; % Identify index number for (data size) < 100
fare_ave=a(2,:); 
fare_ave(tmp)=nan; % Remove data for (data size) < 100
delete result*
elpt=toc
