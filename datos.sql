#--------------------- Carga de datos --------------
#Alumnos: Agustín Fiore Ibarguren y Guillermo Seewald
		
use banco;		
		
INSERT INTO cliente VALUES (1,"Rodriguez", "Manuel","DNI", 15647898, "Estomba 123", 151256212, "1970-10-15");
INSERT INTO cliente VALUES (2, "Figueroa", "Valeria", "DNI", 20147894, "12 de Octubre 452", 159874123, "1980-02-02");
INSERT INTO cliente VALUES (3, "Sanchez", "Ignacio", "DNI", 25564128, "Av. Alem 1542", 155321478, "1985-06-10");	
INSERT INTO cliente VALUES (4, "Bermudez", "Pedro", "DNI", 17489571, "Sarmiento 789", 155781241, "1975-08-21");
INSERT INTO cliente VALUES (5, "Delgado", "Micaela", "DNI", 32578917, "Zapiola 280", 159471471, "1990-10-21");

INSERT INTO ciudad VALUES (8000, "Bahia Blanca");
INSERT INTO ciudad VALUES (7600, "Mar del Plata");
INSERT INTO ciudad VALUES (8109, "Punta Alta");

INSERT INTO sucursal VALUES (1, "Sucursal Alsina", "Alsina 56", "4512312", "8hs a 18hs", 8000);
INSERT INTO sucursal VALUES (2, "Sucursal Av. Colon", "Av. Colon 890", "4547812", "8hs a 16hs", 8000);
INSERT INTO sucursal VALUES (3, "Sucursal Mar del Plata", "Alvarado 512", "4762148", "9hs a 18hs", 7600);
INSERT INTO sucursal VALUES (4, "Sucursal Punta Alta", "Espora 213", "4641521", "8hs a 14hs", 8109);

INSERT INTO caja VALUES (1);
INSERT INTO caja VALUES (2);
INSERT INTO caja VALUES (3);
INSERT INTO caja VALUES (4);
INSERT INTO caja VALUES (100);

INSERT INTO ventanilla VALUES (1, 1);
INSERT INTO ventanilla VALUES (2, 2);
INSERT INTO ventanilla VALUES (4, 3);

INSERT INTO atm VALUES (3, 8000, "11 de Abril 341");

INSERT INTO caja_ahorro VALUES (1, 114569878475645781, 15004567.20);
INSERT INTO caja_ahorro VALUES (2, 289462756416876544, 7050.00);
INSERT INTO caja_ahorro VALUES (3, 381547812548962347, 546128.00);
INSERT INTO caja_ahorro VALUES (4, 441224892318472487, 81347.50);

INSERT INTO cliente_ca VALUES (1, 1);
INSERT INTO cliente_ca VALUES (2, 2);
INSERT INTO cliente_ca VALUES (3, 3);
INSERT INTO cliente_ca VALUES (4, 4);
INSERT INTO cliente_ca VALUES (5, 1);

INSERT INTO transaccion VALUES (1, "2018-05-09", "13:52:05", 15541.50);
INSERT INTO transaccion VALUES (2, "2017-10-15", "10:52:20", 1780);
INSERT INTO transaccion VALUES (3, "2016-12-15", "10:52:05", 200);
INSERT INTO transaccion VALUES (4, "2017-02-12", "09:48:17", 10000);
INSERT INTO transaccion VALUES (5, "2018-06-30", "11:21:00", 5000);
INSERT INTO transaccion VALUES (6, "2018-03-10", "12:01:15", 6500.80);

INSERT INTO transaccion_por_caja VALUES (1, 1);
INSERT INTO transaccion_por_caja VALUES (2, 1);
INSERT INTO transaccion_por_caja VALUES (3, 2);
INSERT INTO transaccion_por_caja VALUES (5, 3);
INSERT INTO transaccion_por_caja VALUES (6, 4);

INSERT INTO extraccion VALUES (1, 1, 1);
INSERT INTO extraccion VALUES (2, 2, 2);

INSERT INTO transferencia VALUES (3, 2, 2, 1);
INSERT INTO transferencia VALUES (6, 5, 1, 4);

INSERT INTO deposito VALUES (5, 4);

INSERT INTO debito VALUES (4, "Transaccion realizada con exito", 3, 3);

INSERT INTO tarjeta VALUES (1, md5('456'), md5('132'), "2020-06-10", 2, 2);

INSERT INTO tasa_plazo_fijo VALUES (30, 60000, 0, 5.5);
INSERT INTO tasa_plazo_fijo VALUES (60, 60000, 0, 6.25);
INSERT INTO tasa_plazo_fijo VALUES (90, 60000, 0, 6.5);
INSERT INTO tasa_plazo_fijo VALUES (120, 60000, 0, 6.75);
INSERT INTO tasa_plazo_fijo VALUES (180, 60000, 0, 7);
INSERT INTO tasa_plazo_fijo VALUES (360, 60000, 0, 7.5);
INSERT INTO tasa_plazo_fijo VALUES (30, 150000, 59999.99, 5.55);
INSERT INTO tasa_plazo_fijo VALUES (60, 150000, 59999.99, 6.3);
INSERT INTO tasa_plazo_fijo VALUES (90, 150000, 59999.99, 6.55);
INSERT INTO tasa_plazo_fijo VALUES (120, 150000, 59999.99, 6.8);
INSERT INTO tasa_plazo_fijo VALUES (180, 150000, 59999.99, 7.05);
INSERT INTO tasa_plazo_fijo VALUES (360, 150000, 59999.99, 7.55);
INSERT INTO tasa_plazo_fijo VALUES (30, 99999999999999, 149999.99, 5.64);
INSERT INTO tasa_plazo_fijo VALUES (60, 99999999999999, 149999.99, 6.39);
INSERT INTO tasa_plazo_fijo VALUES (90, 99999999999999, 149999.99, 6.64);
INSERT INTO tasa_plazo_fijo VALUES (120, 99999999999999, 149999.99, 6.89);
INSERT INTO tasa_plazo_fijo VALUES (180, 99999999999999, 149999.99, 7.14);
INSERT INTO tasa_plazo_fijo VALUES (360, 99999999999999, 149999.99, 7.64);

INSERT INTO plazo_fijo VALUES (1, 6000, "2017-04-10", "2018-04-05", 7.5, 443.83, 1);

INSERT INTO plazo_cliente VALUES (1, 1);

INSERT INTO tasa_prestamo VALUES (6, 0, 3000, 17);
INSERT INTO tasa_prestamo VALUES (12, 0, 3000, 18.5);
INSERT INTO tasa_prestamo VALUES (24, 0, 3000, 20);
INSERT INTO tasa_prestamo VALUES (60, 0, 3000, 25);
INSERT INTO tasa_prestamo VALUES (120, 0, 3000, 30);
INSERT INTO tasa_prestamo VALUES (6, 2999.99, 10000, 20);
INSERT INTO tasa_prestamo VALUES (12, 2999.99, 10000, 21.5);
INSERT INTO tasa_prestamo VALUES (24, 2999.99, 10000, 23);
INSERT INTO tasa_prestamo VALUES (60, 2999.99, 10000, 28);
INSERT INTO tasa_prestamo VALUES (120, 2999.99, 10000, 33);
INSERT INTO tasa_prestamo VALUES (6, 9999.99, 30000, 24);
INSERT INTO tasa_prestamo VALUES (12, 9999.99, 30000, 25.5);
INSERT INTO tasa_prestamo VALUES (24, 9999.99, 30000, 27);
INSERT INTO tasa_prestamo VALUES (60, 9999.99, 30000, 32);
INSERT INTO tasa_prestamo VALUES (120, 9999.99, 30000, 37);

INSERT INTO empleado VALUES (1, "Martinez", "Rodrigo", "DNI", 31512761, "Castelli 1254", 155781263, "Gerente", md5('rodrigo'), 1);

INSERT INTO prestamo VALUES (1, "2017-08-28", 6, 12000, 24, 1020, 2170, 1, 3);

#Lo siguiente ya no es necesario porque hay un trigger implementado
#INSERT INTO pago VALUES (1, 1, "2017-09-28", "2017-09-25");
#INSERT INTO pago VALUES (1, 2, "2017-10-28", "2017-10-25");
#INSERT INTO pago VALUES (1, 3, "2017-11-28", "2017-11-25");
#INSERT INTO pago VALUES (1, 4, "2017-12-28", "2017-12-25");
#INSERT INTO pago VALUES (1, 5, "2018-01-28", NULL);
#INSERT INTO pago VALUES (1, 6, "2018-02-28", NULL);