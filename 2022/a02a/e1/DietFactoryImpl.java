package a02a.e1;

import java.util.*;

public class DietFactoryImpl implements DietFactory {

    /**
	 * @return a diet where a selection of food is ok if:
	 * - overall calories are within range [1500,2000]
	 */
    @Override
    public Diet standard() {
        return new AbstractDiet() {
            private static final int MIN_RANGE = 1500;
            private static final int MAX_RANGE = 2000;

            @Override
            protected boolean checkCondition(final int overAllCalories, final Map<Nutrient, Integer> overAllMacros) {
                return overAllCalories <= MAX_RANGE && overAllCalories >= MIN_RANGE;
            }
        };
    }

    /**
	 * @return a diet where a selection of food is ok if:
	 * - overall calories are within range [1000,1500]
	 * - carbs give overall <=300 calories
	 */
    @Override
    public Diet lowCarb() {
        return new AbstractDiet() {
            private static final int MIN_RANGE = 1000;
            private static final int MAX_RANGE = 1500;
            private static final int MAX_CARBS = 300;

            @Override
            protected boolean checkCondition(final int overAllCalories, final Map<Nutrient, Integer> overAllMacros) {
                final int carbs = overAllMacros.get(Nutrient.CARBS);

                return overAllCalories <= MAX_RANGE && overAllCalories >= MIN_RANGE
                    && carbs <= MAX_CARBS;
            }
        };
    }

    /**
	 * @return a diet where a selection of food is ok if:
	 * - overall calories are within range [2000,2500]
	 * - carbs give overall <=300 calories
	 * - proteins give overall >=1300 calories
	 */
    @Override
    public Diet highProtein() {
        return new AbstractDiet() {
            private static final int MIN_RANGE = 1500;
            private static final int MAX_RANGE = 2500;
            private static final int MAX_CARBS = 300;
            private static final int MIN_PROTEINS = 1300;

            @Override
            protected boolean checkCondition(final int overAllCalories, final Map<Nutrient, Integer> overAllMacros) {
                final int carbs = overAllMacros.get(Nutrient.CARBS);
                final int proteins = overAllMacros.get(Nutrient.PROTEINS);

                return overAllCalories <= MAX_RANGE && overAllCalories >= MIN_RANGE
                    && carbs <= MAX_CARBS && proteins >= MIN_PROTEINS;
            }
        };
    }

    /**
	 * @return a diet where a selection of food is ok if:
	 * - overall calories are within range [1600,2000]
	 * - carbs give overall >=600 calories
	 * - proteins give overall >=600 calories
	 * - fat gives overall >=400 calories
	 * - fat and proteins together give overall <=1100 calories
	 */
    @Override
    public Diet balanced() {
        return new AbstractDiet() {
            private static final int MIN_RANGE = 1600;
            private static final int MAX_RANGE = 2000;
            private static final int MIN_CARBS = 600;
            private static final int MIN_PROTEINS = 600;
            private static final int MIN_FATS = 400;
            private static final int MAX_PROTEINS_FATS = 1110;

            @Override
            protected boolean checkCondition(final int overAllCalories, final Map<Nutrient, Integer> overAllMacros) {
                final int carbs = overAllMacros.get(Nutrient.CARBS);
                final int proteins = overAllMacros.get(Nutrient.PROTEINS);
                final int fats = overAllMacros.get(Nutrient.FAT);
                final int fatAndProtein = proteins + fats;

                return overAllCalories <= MAX_RANGE && overAllCalories >= MIN_RANGE
                    && carbs >= MIN_CARBS && proteins >= MIN_PROTEINS && fats >= MIN_FATS
                    && fatAndProtein <= MAX_PROTEINS_FATS;
            }
        };
    }

    private abstract class AbstractDiet implements Diet {

        private final Map<String, Map<Nutrient, Integer>> foods = new HashMap<>();

        protected abstract boolean checkCondition(final int overAllCalories, final Map<Nutrient, Integer> overAllMacros);

        @Override
        public void addFood(final String name, final Map<Nutrient, Integer> nutritionMap) {
            this.foods.put(name, nutritionMap);
        }

        @Override
        public boolean isValid(final Map<String, Double> dietMap) {
            final var overAllMacros = overAllMacros(dietMap);
            final int overallCalories = overAllCalories(overAllMacros);
            return this.checkCondition(overallCalories, overAllMacros);
        }

        private Map<Nutrient, Integer> overAllMacros(final Map<String, Double> dietMap) {
            final var nutrients = List.of(Nutrient.values());
            final Map<Nutrient, Integer> macroMap = new HashMap<>(nutrients.size());
            for (final var nutrient: nutrients) {
                int totNutrientCal = 0;
                for (final var food: dietMap.entrySet()) {
                    final String name = food.getKey();
                    final Double quantity = food.getValue();
                    totNutrientCal += this.foods.get(name).get(nutrient) * quantity / 100;
                }
                macroMap.put(nutrient, totNutrientCal);
            }
            return macroMap;
        }

        private int overAllCalories(final Map<Nutrient, Integer> overAllMacros) {
            int overallCalories = 0;
            for (final var val: overAllMacros.values()) {
                overallCalories += val;
            }
            return overallCalories;
        }
    };
}
