package com.ulutman.repository;


import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Repository
public interface PublishRepository extends JpaRepository<Publish, Long>, JpaSpecificationExecutor<Publish> {


    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId")
    List<Publish> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE LOWER(p.title) LIKE LOWER(CONCAT( :title, '%'))")
    List<Publish> filterPublishesByTitle(@Param("title") String title);

    @Query("SELECT p FROM Publish p WHERE (:categories IS NULL OR p.category IN :categories)")
    List<Publish> filterPublishesByCategory(@Param("categories") List<Category> categories);

    @Query("SELECT p FROM Publish p WHERE p.publishStatus IN :publishStatuses")
    List<Publish> filterPublishesByStatus(@Param("publishStatuses") List<PublishStatus> publishStatuses);

    @Query("SELECT p FROM Publish p WHERE (:createDates IS NULL OR p.createDate IN :createDates)")
    List<Publish> filterPublishesByCreateDate(@Param("createDates") List<LocalDate> createDates);

    @Query("SELECT p FROM Publish p WHERE (:names IS NULL OR LOWER(p.user.name) LIKE LOWER(CONCAT(:names, '%')))")
    List<Publish> filterPublishesByUserName(@Param("names") String names);

    @Query("SELECT p FROM Publish p WHERE (:categoryStatus IS NULL OR p.categoryStatus IN :categoryStatus)")
    List<Publish> filterPublishesByCategoryStatus(@Param("categoryStatus") List<CategoryStatus> categoryStatus);

    @Query("SELECT COUNT(p) FROM Publish p WHERE p.user.id = :userId")
    Integer countPublicationsByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId")
    List<Publish> filterPublishesByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Publish p WHERE p.user.id = :userId")
    long countByUser(@Param("userId") Long userId);

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('WORK')")
    List<Publish> findByCategoryWork();

    @Query("SELECT  publish FROM Publish publish WHERE  publish.category = 'WORK' AND publish.subCategory= ?1")
    List<Publish> findBySubCategoryWORK(Subcategory subCategory);

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('RENT')")
    List<Publish> findByCategoryRent();

    @Query("SELECT publish FROM Publish  publish WHERE publish.category = 'RENT' AND publish.subCategory= ?1")
    List<Publish> findBySubCategoryRent(Subcategory subCategory);

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('SELL')")
    List<Publish> findByCategorySell();

    @Query("SELECT publish FROM Publish publish WHERE publish.category='SELL' AND publish.subCategory = ?1")
    List<Publish> findBySubCategorySell(Subcategory subCategory);

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('HOTEL')")
    List<Publish> findByCategoryHotel();

    @Query("SELECT publish FROM Publish publish WHERE publish.category = 'HOTEL' AND  publish.subCategory =?1")
    List<Publish> findBySubCategoryHotel(Subcategory subCategory);

    @Query("SELECT publish FROM Publish publish WHERE publish.category =('AUTO')")
    List<Publish> findByCategoryServices();

    @Query("SELECT publish FROM Publish  publish WHERE publish.category = 'AUTO' AND publish.subCategory =?1")
    List<Publish> findBySubCategoryAUTO(Subcategory subCategory);

    @Query("SELECT publish FROM Publish  publish WHERE publish.category = 'SERVICES' AND publish.subCategory =?1")
    List<Publish> findBySubCategoryServices(Subcategory subCategory);

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('REAL_ESTATE')")
    List<Publish> findByCategoryRealEstate();

    @Query("SELECT publish FROM Publish publish WHERE publish.category = 'REAL_ESTATE' AND publish.subCategory = ?1")
    List<Publish> findBySubCategoryREAL_ESTATE(Subcategory subCategory);

    @Query("SELECT p FROM Publish p WHERE (:categories IS NULL OR p.category IN :categories) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'newest' THEN p.createDate END DESC, " +
            "CASE WHEN :sortBy = 'cheapest' THEN p.price END ASC, " +
            "CASE WHEN :sortBy = 'expensive' THEN p.price END DESC")
    List<Publish> filterPublishesByCategory(
            @Param("categories") List<Category> categories,
            @Param("sortBy") String sortBy
    );

    @Query("SELECT p FROM Publish p " +
            "JOIN p.propertyDetails pd " +
            "WHERE (:minTotalArea IS NULL OR pd.totalArea >= :minTotalArea) " +
            "AND (:maxTotalArea IS NULL OR pd.totalArea <= :maxTotalArea) " +
            "AND (:minKitchenArea IS NULL OR pd.kitchenArea >= :minKitchenArea) " +
            "AND (:maxKitchenArea IS NULL OR pd.kitchenArea <= :maxKitchenArea) " +
            "AND (:minLivingArea IS NULL OR pd.livingArea >= :minLivingArea) " +
            "AND (:maxLivingArea IS NULL OR pd.livingArea <= :maxLivingArea) " +
            "AND (:minYear IS NULL OR pd.yearOfConstruction >= :minYear) " +
            "AND (:maxYear IS NULL OR pd.yearOfConstruction <= :maxYear) " +
            "AND (:transportType IS NULL OR pd.transportType = :transportType) " +
            "AND (:walkingDistance IS NULL OR pd.walkingDistance <= :walkingDistance) " +
            "AND (:transportDistance IS NULL OR pd.transportDistance <= :transportDistance)")
    List<Publish> filterPublishes(
            @Param("minTotalArea") Double minTotalArea,
            @Param("maxTotalArea") Double maxTotalArea,
            @Param("minKitchenArea") Double minKitchenArea,
            @Param("maxKitchenArea") Double maxKitchenArea,
            @Param("minLivingArea") Double minLivingArea,
            @Param("maxLivingArea") Double maxLivingArea,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("transportType") TransportType transportType,
            @Param("walkingDistance") Double walkingDistance,
            @Param("transportDistance") Double transportDistance
    );

    @Query("SELECT p FROM Publish p WHERE p.createdAt < :expirationTime")
    List<Publish> findAllByCreatedAtBefore(@Param("expirationTime") LocalDateTime expirationTime);

    List<Publish> findAll(Specification<Publish> specification);

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId AND p.active = false")
    List<Publish> findByUserIdAndActiveFalse(@Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId AND p.publishStatus = :status")
    List<Publish> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") PublishStatus status);

    @Query("SELECT p FROM Publish p WHERE p.active = false")
    List<Publish> findAllByActiveFalse();

    @Query("SELECT p FROM Publish p WHERE p.active = true ORDER BY p.lastBoostedAt DESC NULLS LAST")
    List<Publish> findAllActivePublishes();

    @Query("SELECT p FROM Publish p WHERE p.id = :id AND p.user.id = :userId")
    Publish findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE p.active = true AND p.user.id = :userId ORDER BY p.lastBoostedAt DESC NULLS LAST")
    List<Publish> findAllActivePublishesByUserId(Long userId);
}




