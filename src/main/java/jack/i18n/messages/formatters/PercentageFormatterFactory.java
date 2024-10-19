package jack.i18n.messages.formatters;

/**
 * Factory for creating {@link PercentageFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class PercentageFormatterFactory extends AbstractDecimalFamilyFactory<PercentageFormatter> {

    @Override
    protected PercentageFormatter createFormatterInternal(int decimalPlaces, int decimalPadding, boolean rounding)
            throws FormatErrorException {
        return new PercentageFormatter(decimalPlaces, decimalPadding, rounding);
    }
}
