package jack.i18n.messages.formatters;

/**
 * A factory that creates {@link DecimalFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class DecimalFormatterFactory extends AbstractDecimalFamilyFactory<DecimalFormatter> {

    @Override
    protected DecimalFormatter createFormatterInternal(int decimalPlaces, int decimalPadding, boolean rounding) {
        return new DecimalFormatter(decimalPlaces, decimalPadding, rounding);
    }

}
