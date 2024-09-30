package jack.i18n.messages.formatters;

/**
 * A factory for creating {@link CurrencyFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class CurrencyFormatterFactory extends AbstractDecimalFamilyFactory<CurrencyFormatter> {

    protected CurrencyFormatter createFormatterInternal(int decimalPlaces, int decimalPadding, boolean rounding) {
        return new CurrencyFormatter(decimalPlaces, decimalPadding, rounding);
    }

}
