clear;

addpath('/cis/public/datasci/jikken/L8data');

p=parpool(16);
inPool=mapreducer(p);

tdir='/cis/public/datasci/class/taxi_yellow/2017/';
path1={[tdir 'yellow_tripdata_2017-01.csv'],...
    [tdir 'yellow_tripdata_2017-02.csv'],...
    [tdir 'yellow_tripdata_2017-03.csv'],...
    [tdir 'yellow_tripdata_2017-04.csv'],...
    [tdir 'yellow_tripdata_2017-05.csv'],...
    [tdir 'yellow_tripdata_2017-06.csv'],...
    [tdir 'yellow_tripdata_2017-07.csv'],...
    [tdir 'yellow_tripdata_2017-08.csv'],...
    [tdir 'yellow_tripdata_2017-09.csv'],...
    [tdir 'yellow_tripdata_2017-10.csv'],...
    [tdir 'yellow_tripdata_2017-11.csv'],...
    [tdir 'yellow_tripdata_2017-12.csv']};

tic
ds=datastore(path1);
ds.SelectedVariableNames={'PULocationID', 'total_amount', 'trip_distance'};

ticBytes(gcp)
output=mapreduce(ds,@statmapper2,@statreducer2,inPool);
tocBytes(gcp)

r=readall(output); % Read output data
id=1:265; % Define number of zone
a=[id;r.Value{:}.farepertrdi]; % Create array of ID and fare
tmp=r.Value{:}.num<100; % Identify index number for (data size) < 100
fare_ave=a(2,:); 
fare_ave(tmp)=nan; % Remove data for (data size) < 100
delete result*
elpt=toc