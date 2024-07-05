select p.name Работник, c.name Компания 
from person p
left join company c on p.company_id = c.id
where p.company_id != 5;