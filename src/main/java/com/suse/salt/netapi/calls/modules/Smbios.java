package com.suse.salt.netapi.calls.modules;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.suse.salt.netapi.calls.LocalCall;

import com.google.gson.reflect.TypeToken;

/**
 * salt.modules.smbios
 */
public class Smbios {

    /**
     * The type of the dmi records.
     */
    public enum RecordType {

        BIOS(0),
        SYSTEM(1),
        BASEBOARD(2),
        CHASSIS(3),
        PROCESSOR(4),
        MEMORY_CONTROLLER(5),
        MEMORY_MODULE(6),
        CACHE(7),
        PORT_CONNECTOR(8),
        SYSTEM_SLOTS(9),
        ON_BOARD_DEVICES(10),
        OEM_STRINGS(11),
        SYSTEM_CONFIGURATION_OPTIONS(12),
        BIOS_LANGUAGE(13),
        GROUP_ASSOCIATIONS(14),
        SYSTEM_EVENT_LOG(15),
        PHYSICAL_MEMORY_ARRAY(16),
        MEMORY_DEVICE(17),
        BIT32_MEMORY_ERROR(18),
        MEMORY_ARRAY_MAPPED_ADDRESS(19),
        MEMORY_DEVICE_MAPPED_ADDRESS(20),
        BUILTIN_POINTING_DEVICE(21),
        PORTABLE_BATTERY(22),
        SYSTEM_RESET(23),
        HARDWARE_SECURITY(24),
        SYSTEM_POWER_CONTROLS(25),
        VOLTAGE_PROBE(26),
        COOLING_DEVICE(27),
        TEMPERATURE_PROBE(28),
        ELECTRICAL_CURRENT_PROBE(29),
        OUTOFBAND_REMOTE_ACCESS(30),
        BOOT_INTEGRITY_SERVICES(31),
        SYSTEM_BOOT(32),
        BIT64_MEMORY_ERROR(33),
        MANAGEMENT_DEVICE(34),
        MANAGEMENT_DEVICE_COMPONENT(35),
        MANAGEMENT_DEVICE_THRESHOLD_DATA(36),
        MEMORY_CHANNEL(37),
        IPMI_DEVICE(38),
        POWER_SUPPLY(39),
        ADDITIONAL_INFORMATION(40),
        ONBOARD_DEVICES_EXTENDED_INFORMATION(41),
        MANAGEMENT_CONTROLLER_HOST_INTERFACE(42);

        private int type;

        RecordType(int typeId) {
            this.type = typeId;
        }

        /**
         * @return the corresponding integer
         */
        public int getType() {
            return type;
        }
    }

    private Smbios() { }

    /**
     * Holds the information returned by smbios.records
     */
    public static class Record {

        private Map<String, Object> data;
        private String description;
        private String handle;
        private int type;

        /**
         * Get the actual data returned by smbios.records
         * @return a map with the data
         */
        public Map<String, Object> getData() {
            return data;
        }

        /**
         * @return the DMI record description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the DMI record handle
         */
        public String getHandle() {
            return handle;
        }

        /**
         * @return the DMI record type
         */
        public int getType() {
            return type;
        }
    }

    /**
     * smbios.records
     * @param type the type of the record to get or null to get all records
     * @return The {@link LocalCall} to use for getting the record(s)
     */
    public static LocalCall<List<Record>> records(RecordType type) {
        Map<String, Object> args = new LinkedHashMap<>();
        if (type != null) {
            args.put("rec_type", type.getType());
        }
        args.put("clean", false);
        return new LocalCall<>("smbios.records", Optional.empty(), Optional.of(args),
                new TypeToken<List<Record>>() { });
    }

}
