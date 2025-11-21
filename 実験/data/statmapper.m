function statmapper(data,~,intermKVStore)

fare=data.total_amount;
puid=data.PULocationID;

tmpid=fare>300|fare<=0; %Identify index number for anomaly
fare(tmpid)=[]; % Remove data for anomaly
puid(tmpid)=[]; % Remove data for anomaly

for z=1:265
    tmp=puid==z;
    tmp2=fare(tmp);
    fare_sum(z)=sum(tmp2); % Calculate sum of fare for each zone
    fare_num(z)=size(tmp2,1); % Calculate number of sample for each zone
end
% Save results of map function that is subsequently read in reduce function
add(intermKVStore,'key',{[fare_sum' fare_num']}); 
