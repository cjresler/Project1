drop view one_connection;
create view one_connection (src,dst,dep_date,flightno1,flightno2, layover,price) as
  select a1.src, a2.dst, a1.dep_date, a1.flightno, a2.flightno, a2.dep_time-a1.arr_time,
  min(a1.price+a2.price)
  from available_flights a1, available_flights a2
  where a1.dst=a2.src and a1.arr_time +1.5/24 <=a2.dep_time and a1.arr_time +5/24 >=a2.dep_time
  group by a1.src, a2.dst, a1.dep_date, a1.flightno, a2.flightno, a2.dep_time, a1.arr_time;
