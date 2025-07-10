package com.store.Bookwire.validators;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.StringUtils.capitalize;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private String fieldName;
    private Class<?> entityClass;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        try {
            Object fieldValue = getFieldValue(dto, fieldName);
            if (fieldValue == null) {
                return true;
            }

            List<Long> foundIds = findEntityIdsByField(fieldValue);

            Object currentId = getCurrentId(dto);
            boolean isUpdate = currentId != null;

            boolean notUnique = isNotUnique(foundIds, currentId, isUpdate);

            if (notUnique) {
                buildConstraintViolation(context);
                return false;
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldValue(Object dto, String fieldName) throws Exception {
        return dto.getClass()
                .getMethod("get" + capitalize(fieldName))
                .invoke(dto);
    }

    private List<Long> findEntityIdsByField(Object value) {
        String jpql = "SELECT e.id FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("value", value)
                .setMaxResults(1);
        return query.getResultList();
    }

    private Object getCurrentId(Object dto) {
        try {
            return dto.getClass().getMethod("getId").invoke(dto);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNotUnique(List<Long> foundIds, Object currentId, boolean isUpdate) {
        if (foundIds.isEmpty()) {
            return false;
        }

        Long foundId = foundIds.getFirst();

        if (!isUpdate) {
            return true;
        }

        return !foundId.equals(currentId);
    }

    private void buildConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(fieldName)
                .addConstraintViolation();
    }
}

