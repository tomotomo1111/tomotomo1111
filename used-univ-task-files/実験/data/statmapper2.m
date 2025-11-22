function statmapper2(data,~,intermKVStore)

fare=data.total_amount;
puid=data.PULocationID;
trdi=data.trip_distance;

tmpid=fare>300|fare<=0|trdi<=0.5|trdi>100; %Identify index number for anomaly
fare(tmpid)=[]; % Remove data for anomaly
puid(tmpid)=[]; % Remove data for anomaly
trdi(tmpid)=[];

for z=1:265
    tmp=puid==z;
    tmp2=fare(tmp);
    tmp3=trdi(tmp);
    fare_sum(z)=sum(tmp2); % Calculate sum of fare for each zone
    fare_num(z)=size(tmp2,1); % Calculate number of sample for each zone
    trdi_sum(z)=sum(tmp3);
end
% Save results of map function that is subsequently read in reduce function
add(intermKVStore,'key',{[fare_sum' fare_num' trdi_sum']}); 
