package student;
import java.math.BigDecimal;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {
    static double euclidean(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    static BigDecimal getPackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
        percentage = percentage.divide(new BigDecimal(100));
        switch (type) {
            case 0: {
                return new BigDecimal(10.0 * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 1: {
                return new BigDecimal((25.0 + weight.doubleValue() * 100.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 2: {
                return new BigDecimal((75.0 + weight.doubleValue() * 300.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
        }
        return null;
    }
    public static void main(String[] args) {
        CityOperations cityOperations = new na170044_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new na170044_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new na170044_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new na170044_CourierRequestOperation();
        GeneralOperations generalOperations = new na170044_GeneralOperations();
        UserOperations userOperations = new na170044_UserOperations();
        VehicleOperations vehicleOperations = new na170044_VehicleOperations();
        PackageOperations packageOperations = new na170044_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
        /*GeneralOperations ge=new na170044_GeneralOperations();
        ge.eraseAll();
        String courierLastName = "Ckalja";
        String courierFirstName = "Pero";
        String courierUsername = "perkan";
        String password = "sabi2018";
        UserOperations user= new na170044_UserOperations();
        user.insertUser(courierUsername, courierFirstName, courierLastName, password);
        System.out.print(user.getAllUsers());
        String licencePlate = "BG323WE";
        int fuelType = 0;
        BigDecimal fuelConsumption = new BigDecimal(8.3);
        VehicleOperations vehicle=new na170044_VehicleOperations();
        vehicle.insertVehicle(licencePlate, fuelType, fuelConsumption);
        CourierRequestOperation courierRequest=new na170044_CourierRequestOperation();
        courierRequest.insertCourierRequest(courierUsername, licencePlate);
        courierRequest.grantRequest(courierUsername);
        CourierOperations courier=new na170044_CourierOperations();
        System.out.println(courier.getAllCouriers());
        String senderUsername = "masa";
        String senderFirstName = "Masana";
        String senderLastName = "Leposava";
        password = "lepasampasta1";
        user.insertUser(senderUsername, senderFirstName, senderLastName, password);
        CityOperations city=new na170044_CityOperations();
        int cityId = city.insertCity("Novo Milosevo", "21234");
        int cordXd1 = 10;
        int cordYd1 = 2;
        DistrictOperations district=new na170044_DistrictOperations();
        int districtIdOne = district.insertDistrict("Novo Milosevo", cityId, cordXd1, cordYd1);
        int cordXd2 = 2;
        int cordYd2 = 10;
        int districtIdTwo = district.insertDistrict("Vojinovica", cityId, cordXd2, cordYd2);
        int type1 = 0;
        BigDecimal weight1 = new BigDecimal(123);
        PackageOperations paket=new na170044_PackageOperations();
        int packageId1 = paket.insertPackage(districtIdOne, districtIdTwo, courierUsername, type1, weight1);
        BigDecimal packageOnePrice = getPackagePrice(type1, weight1, euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        int offerId = paket.insertTransportOffer(courierUsername, packageId1, new BigDecimal(5));
        paket.acceptAnOffer(offerId);
        int type2 = 1;
        BigDecimal weight2 = new BigDecimal(321);
        int packageId2 = paket.insertPackage(districtIdTwo, districtIdOne, courierUsername, type2, weight2);
        BigDecimal packageTwoPrice = getPackagePrice(type2, weight2, euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = paket.insertTransportOffer(courierUsername, packageId2, new BigDecimal(5));
        paket.acceptAnOffer(offerId);
        int type3 = 1;
        BigDecimal weight3 = new BigDecimal(222);
        int packageId3 = paket.insertPackage(districtIdTwo, districtIdOne, courierUsername, type3, weight3);
        BigDecimal packageThreePrice = getPackagePrice(type3, weight3, euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = paket.insertTransportOffer(courierUsername, packageId3, new BigDecimal(5));
        paket.acceptAnOffer(offerId);
        System.out.print("1 ");
        System.out.println(paket.getDeliveryStatus(packageId1).intValue());
        System.out.print(packageId1+" ");
        System.out.println(paket.driveNextPackage(courierUsername));
        System.out.print(3+" ");
        System.out.println(paket.getDeliveryStatus(packageId1).intValue());
        System.out.print(2+" ");
        System.out.println(paket.getDeliveryStatus(packageId2).intValue());
        System.out.print(packageId2+" ");
        System.out.println(paket.driveNextPackage(courierUsername));
        System.out.print(3+" ");
        System.out.println(paket.getDeliveryStatus(packageId2).intValue());
        System.out.print(2+" ");
        System.out.println(paket.getDeliveryStatus(packageId3).intValue());
        System.out.print(packageId3+" ");
        System.out.println(paket.driveNextPackage(courierUsername));
        System.out.print(3+" ");
        System.out.println(paket.getDeliveryStatus(packageId3).intValue());
        BigDecimal gain = packageOnePrice.add(packageTwoPrice).add(packageThreePrice);
        BigDecimal loss = new BigDecimal(euclidean(cordXd1, cordYd1, cordXd2, cordYd2) * 4.0 * 15.0).multiply(fuelConsumption);
        BigDecimal actual = courier.getAverageCourierProfit(0);
        System.out.print(gain.subtract(loss)+" ");
        System.out.println(actual);
        System.out.println(packageOnePrice+" "+packageTwoPrice+" "+packageThreePrice);*/
    }
}
