
select orderdetail.orderId, orderinfo.name, orderinfo.address,orderinfo.date, group_concat(item.description) as items, (sum(orderdetail.qty*orderdetail.unitPrice)) as total_price
from orderdetail
inner join item
on orderdetail.itemCode= item.code
inner join (select orders.id as orderID, customer.name, customer.address, orders.date
			from orders
			inner join customer 
			on orders.customerId = customer.id) as orderinfo
on orderdetail.orderId = orderinfo.orderId
group by orderdetail.orderId;

