package jack.i18n.messages;

/**
 * Factory for creating {@link PercentageFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class PercentageFormatterFactory extends AbstractDecimalFamilyFactory<PercentageFormatter> {

    @Override
    protected PercentageFormatter createFormatterInternal(Integer decimalPlaces) throws FormatErrorException {
        return new PercentageFormatter(decimalPlaces);
    }
}
