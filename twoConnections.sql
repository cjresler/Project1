drop view good_connections;
create view good_connections (src,dst,dep_date,flightno1,flightno2, layover,price) as
  select a1.src, a3.dst, a1.dep_date, a1.flightno, a2.flightno, a3.flightno, (a2.dep_time-a1.arr_time+(a3.dep_time-a2.arr_time)),
  min(a1.price+a2.price+a2.price)
  from available_flights a1, available_flights a2, available_flights a3
  where a1.dst=a2.src and a2.dst=a3.src and a1.arr_time +1.5/24 <=a2.dep_time and a1.arr_time +5/24 >=a2.dep_time and a2.arr_time +1.5/24 <=a3.dep_time and a2.arr_time +5/24 >=a3.dep_time
  group by a1.src, a3.dst, a1.dep_date, a1.flightno, a2.flightno, a2.dep_time, a1.arr_time, a3.dep_time, a2.arr_time;
