package grabber;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class HabrCareerParseTest {

   @Test
    void parseVacancyText() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder expected = new StringBuilder();
        expected.append("О компании и команде\n");
        expected.append("Сейчас мы расширяем команды и ищем разработчиков для развития нескольких сервисов: Тинькофф Инвестиции. Мы — лидер среди брокеров по количеству активных клиентов. Делаем инвестиции удобными, технологичными и понятными. Тинькофф Бизнес. Меняем подход к ведению бухгалтерии, обмену документами между организациями и работе с архивами — переводим все и вся в цифру. Тинькофф Страхование. Развиваем платформу прямых продаж. Наша цель — предсказуемый, удобный процесс для бизнеса и клиента. Платежные технологии и процессинг. Занимаемся разработкой и поддержкой платежного шлюза банка. Задачи шлюза – определять тип платежей, наполнять их данными из других систем банка и проверять разрешенность операций. У нас много интересных и разнообразных задач, опытная команда и отличные возможности для роста. Откликайтесь на вакансию, чтобы узнать о проектах и выбрать подходящий для вас. Обязанности Разрабатывать внешние и внутренние продукты Прорабатывать и реализовать интеграционные решения\n");
        expected.append("Ожидания от кандидата\n");
        expected.append("Опыт разработки на Java от 3 лет Опыт коммерческой разработки на Java 11+ или Kotlin Опыт коммерческой разработки с любым из фреймворков: Spring Boot, Quarkus, Micronaut или Vert.x Опыт коммерческой разработки с одним из контейнеризаторов: Kubernetes, Docker или OpenShift Опыт коммерческой разработки с одним из брокеров: Kafka, Rabbit MQ или Active MQ Опыт коммерческой разработки с Postgress, MySQL или Oracle будет плюсом Опыт работы с системой контроля версий\n");
        expected.append("Условия работы\n");
        expected.append("Возможность работы в аккредитованной ИТ-компании Работа в офисе или удаленно. График работы — гибридный Платформа обучения и развития Тинькофф Апгрейд. Курсы, тренинги, вебинары и базы знаний. Поддержка менторов и наставников, помощь в поиске точек роста и карьерном развитии Забота о здоровье. Оформим полис ДМС со стоматологией и страховку от несчастных случаев. Предложим льготное страхование вашим близким Компенсация затрат на спорт 3 дополнительных дня отпуска в год Достойную зарплату — обсудим ее на собеседовании\n");
        HabrCareerParse hp = new HabrCareerParse();
       Class<?> clazz = HabrCareerParse.class;
       Method privateMethod = clazz.getDeclaredMethod("retrieveDescription", String.class);
       privateMethod.setAccessible(true);
       Object result = privateMethod.invoke(hp, "https://career.habr.com/vacancies/1000141134");
        assertEquals(expected.toString(), result.toString());
   }

}