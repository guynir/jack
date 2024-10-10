package jack.i18n.messages.formatters;

/**
 * A factory for creating {@link CurrencyFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class CurrencyFormatterFactory extends AbstractDecimalFamilyFactory<CurrencyFormatter> {

    @Override
    protected CurrencyFormatter createFormatterInternal(Integer decimalPlaces) throws FormatErrorException {
        return new CurrencyFormatter(decimalPlaces);
    }

}
