select c.name Компания, COUNT(p.name) Работники
from company c
join person p on p.company_id = c.id
Group by c.name
HAVING 
    COUNT(p.name) = (
        SELECT 
            MAX(company_counts.num_people)
        FROM (
            SELECT 
                COUNT(p.name) AS num_people
            FROM 
                company c
            LEFT JOIN 
                person p ON c.id = p.company_id
            GROUP BY 
                c.id
        ) AS company_counts
    );