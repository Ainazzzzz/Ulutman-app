package com.ulutman.model.enums;

import java.util.Arrays;
import java.util.List;

import static com.ulutman.model.enums.Subcategory.*;

public enum Category {

    WORK {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.PartTime,
                    FullTime);
        }
    },
    RENT {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.I_RentRoom,
                    I_RentBed,
                    I_RenApartment,
                    RentRoom,
                    RentBed,
                    RentApartment,
                    RentOffice);
        }
    },
    SELL {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.Clothes,
                    HouseAppliances,
                    Electronics);
        }
    },
    HOTEL {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.DailyRent,
                    LongTermRent);
        }
    },
    SERVICES {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.MEDICAL,
                    LEGAL,
                    BEAUTY,
                    AIRTICKET,
                    TAXIANDTRACK,
                    REPAIR,
                    DIFFERENT);
        }
    },
    AUTO {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.SaleOfCar, Subcategory.RentOfCar);
        }
    },
    REAL_ESTATE {
        @Override
        public List<Subcategory> getSubcategories() {
            return Arrays.asList(Subcategory.House,
                    Apartment,
                    PartOfLand,
                    Space);
        }
    };

    public abstract List<Subcategory> getSubcategories();

    public static List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }

    public static List<Subcategory> getAllSubcategories(Category category) {
        return category.getSubcategories();
    }
}

